(ns app.tutor
  (:require
   [reagent.core :as r]
   ["sql.js/dist/sql-wasm.js" :as sqljs]))

(def instructions
  [{:explanation "A database has already been created, run the following query to create a table"
    :query "CREATE TABLE employees(id integer,  name text,designation text,manager integer,hired_on date,salary  integer,commission float,dept integer);"}
   {:explanation "you completed the tutorials"}])

(defn tutor []
  (let [SQL (r/atom nil)
        db (r/atom nil)
        instruction-index (r/atom 0)
        input-query (r/atom "")]
    (.then
     (.then
      (sqljs {:locateFile "http://localhost:3000/sql-wasm.wasm"})
      #(reset! SQL %))
     #(let [Database (.-Database @SQL)]
        (reset! db (Database.))))
    (fn []
      (let [query (nth instructions @instruction-index)]
        [:div
         [:h1 "SQL Tutorial"]
         [:p (:explanation query)]
         [:p (:query query)]
         [:textarea {:rows 4 :cols 50
                     :value @input-query
                     :on-change #(reset! input-query (.-value (.-target %)))}]
         [:button
          {:on-click
           (fn []
             (try
               (.run @db @input-query)
               (reset! input-query "")
               (swap! instruction-index inc)
               (catch js/Error e (js/console.log (.-message e)))))}
          "execute"]]))))
