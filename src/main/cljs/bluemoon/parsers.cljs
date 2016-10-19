(ns bluemoon.parsers
	(:require
		[om.next :as om]
		[cognitect.transit :as transit]))

(defn cb-handler [cb e]
	(let [response (transit/read (transit/reader :json)
		(.. e -currentTarget -responseText))]
	(cb response)))

(defn send [{query :remote} cb]
	(let [request-body (transit/write (transit/writer :json) query)]
		(doto (js/XMLHttpRequest.)
			(.open "POST" "/bluemoon/om-query")
			(.addEventListener "load" #(cb-handler cb %))
			(.send request-body))))

(defmulti mutate om/dispatch)

(defmethod mutate 'prepend-name
	[env k params]
	(let [{:keys [state ast]} env]
		{:action #(swap! state update :names conj (:name params))
			:remote ast}))

(defmulti read om/dispatch)

(defmethod read :names
	[env k params]
	(let [limit (:limit params)
		state (:state env)]
		{:value (take limit (:names @state))}))

(defmethod read :server-time
	[env k params]
	(let [{:keys [state ast]} env]
		{:value (get @state k)
			:remote ast}))

(defmethod read :current-view
	[env k params]
	(let [{:keys [state ast]} env]
		{:value (get @state k)}))

(def parser
	(om/parser
		{:read read
	     :mutate mutate}))
