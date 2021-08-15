(ns app.frontend.subs
  (:require [re-frame.core :as re-frame :refer [reg-sub]]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(reg-sub
 ::table
 (fn-traced [db]
   (:all-data db)))
