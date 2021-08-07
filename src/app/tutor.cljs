(ns app.tutor
  (:require
   [reagent.core :as r]
   ["sql.js/dist/sql-wasm.js" :as sqljs]))

(defn tutor []
  (let [SQL (r/atom nil)
        db (r/atom nil)
        query "CREATE TABLE employees(id integer,  name text,designation text,manager integer,hired_on date,salary  integer,commission float,dept integer);"]
    (.then
     (.then
      (sqljs {:locateFile "http://localhost:3000/sql-wasm.wasm"})
      #(reset! SQL %))
     #(let [Database (.-Database @SQL)]
       (reset! db (Database.))))
    (fn []
      [:div
       [:h1 "SQL Tutorial"]
       [:p "A database has already been created, run the following query to create a table"]
       [:p query]
       [:textarea {:rows 4 :cols 50
                   :on-key-press (fn [e]
                                   (when (= 13 (.-charCode e))
                                     (try
                                       (.run @db
                                             (.-value
                                              (.-target e)))
                                       (catch js/Error e (js/console.log (.-message e))))))}
        ]
       ;; visually show what's in the database
       ])))
