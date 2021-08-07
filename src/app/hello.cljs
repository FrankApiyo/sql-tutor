(ns app.hello
  (:require
   [reagent.core :as r]
   ["sql.js/dist/sql-wasm.js" :as sqljs]))

(defn hello []
  (let [SQL (r/atom nil)]
    (.then
     (sqljs {:locateFile "http://localhost:3000/sql-wasm.wasm"})
     #(reset! SQL %))
    (fn []
      [:h1 "SQL Tutorial"])))
