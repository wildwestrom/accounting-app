(ns app.frontend.core-test
  (:require [app.frontend.core :as sut]
            [cljs.test :as t :include-macros true
             :refer [deftest testing is]]))

(deftest dummy-test
  (is (= true true)))
