'use strict';
App.controller('AppController', ['$scope', '$http', function($scope, $http) {
    var self  = this;
    self.logout = function(){
        $http({
            method: 'GET',
            url: 'logout'
        }).then(function successCallback(response) {
            console.log("s",response);
            // this callback will be called asynchronously
            // when the response is available
        }, function errorCallback(response) {
            console.log("e",response);
            // called asynchronously if an error occurs
            // or server returns response with an error status.
        });
    }
}]);