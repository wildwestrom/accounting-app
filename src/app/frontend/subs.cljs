(ns app.frontend.subs
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(rf/reg-sub
 ::table
 (fn-traced [db]
   (:all-data db)))
