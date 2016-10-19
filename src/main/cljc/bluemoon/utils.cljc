(ns bluemoon.utils)

(defn ->int
  "文字列を数値型に変換する"
  [val]
  #?(
      :clj  (some->> val str Integer/parseInt)
      :cljs (some->> val str js/parseInt)))

(defn parseDate
  "yyyyMMdd文字列からDateを生成"
  [yyyyMMdd]
  #?(
      :clj  nil
      :cljs (if (not (== (count yyyyMMdd) 8))
              (throw "Invalid date format.")
              (let [yyyy (.substr yyyyMMdd 0 4)
                    MM   (- (js/parseInt (.substr yyyyMMdd 4 2)) 1)
                    dd   (.substr yyyyMMdd 6 2)]
                (js/Date. yyyy MM dd)))))

(defn convert-list-to-id-map
  "リストを各リストのIDをキーとするマップに変換する。
  [{:id 1 :name \"name1\"} {:id 2 :name \"name2\"}]
  -> {:1 {:name \"name1\"} :2 {:name \"name2\"}}"
  [list & [id-key _]]
  (let [id-key (if id-key id-key :id)]
    (reduce #(if (nil? (key %2))
               #?(
                   :clj  (throw (Exception. "no id data."))
                   :cljs (throw "no id data."))
               (if (< 1 (count (val %2)))
                 #?(
                     :clj  (throw (Exception. "duplicate key."))
                     :cljs (throw "duplicate key."))
                 (assoc %1 (keyword (str (key %2))) (first (val %2)))))
            {} (group-by id-key list))))

(defn get-task-in-calendar
  "タスクとカレンダーからタスクに下記の属性を付与する。
  calendarは日付ソートされている前提。

  :start-date    タスク完了日
  :end-date      タスク完了日
  :period        タスク継続日数（休日も含む）
  :rest-calendar 残カレンダー"
  [{:keys [man_hour] :as task} calendar]
  (merge task
         (if (> (:rest-running (first calendar)) man_hour)
           {:start-date (:yyyymmdd (first calendar))
            :end-date (:yyyymmdd (first calendar))
            :period (/ man_hour
                       (:running (first calendar)))
            :rest-calendar (cons (update-in (first calendar) [:rest-running] #(- % man_hour))
                                 (rest calendar))}
           (loop [i-date 0
                  rest-calendar calendar
                  total-running (:rest-running (first calendar))]
             (if (> total-running man_hour)
               {:start-date (:yyyymmdd (first calendar))
                :end-date (:yyyymmdd (first rest-calendar))
                :period (+ (if (zero? (:running (first calendar)))
                             1
                             (/ (:rest-running (first calendar))
                                (:running (first calendar))))
                           (dec i-date)
                           (/ (- (:rest-running (first rest-calendar))
                                 (- total-running man_hour))
                              (:running (first rest-calendar))))
                :rest-calendar (cons (assoc (first rest-calendar) :rest-running (- total-running man_hour))
                                     (rest rest-calendar))}
               (recur (inc i-date)
                      (rest rest-calendar)
                      (+ total-running (:rest-running (first (rest rest-calendar))))))))))

(defn get-tasks-in-calendar
  "カレンダー中のタスク一覧を取得する。
  各タスクに下記属性を付与する。

  :start-date タスク開始予定日
  :end-date   タスク終了予定日
  :period     カレンダー上での1日に対して相対的な表示長"
  [tasks calendar]
  (let [tasks    (sort-by :task_order tasks)
        calendar (->> calendar
                      (map #(assoc % :rest-running (:running %)))
                      (sort-by :yyyymmdd))]
    ;; 各タスクに対して再起処理(終了条件:全タスク割当完了かカレンダー日付がなくなった場合)
    (loop [result        []
           rest-tasks    tasks
           rest-calendar calendar]
      (if (or (empty? rest-tasks) (empty? rest-calendar))
        result
        (let [{:keys [rest-calendar] :as task} (get-task-in-calendar (first rest-tasks) rest-calendar)]
          (recur (conj result (dissoc task :rest-calendar))
                 (rest rest-tasks)
                 rest-calendar))))))
