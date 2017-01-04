angular.module('auth', []).factory( 'AuthService', ['$http', '$location', function($http, $location) {
    var auth = {
        authenticated : false,
        isAdmin: false,
        username: null,
        homePath : '/',
        login : function(credentials, callback) {
            $http({
                method: 'POST',
                url: 'sec/login',
                data: 'username='+ credentials.username+'&password='+credentials.password,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then(function successCallback(response) {
                if(response.data.status){
                    auth.isAdmin = response.data.isAdmin;
                    auth.authenticated = true;
                    auth.username = credentials.username;
                    callback && callback();
                }else{
                    auth.authenticated = false;
                    callback && callback(response.data.error);
                }
            }, function errorCallback() {
                auth.authenticated = false;
                callback && callback("Chyba při zpracování přihlášení.");
            });
        },
        logout : function(callback) {
            $http({
                method: 'POST',
                url: 'sec/logout',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then(function successCallback(response) {
                auth.isAdmin = false;
                auth.authenticated = false;
                auth.username = null;
                callback && callback();
            }, function errorCallback() {
                callback && callback("Chyba při zpracování odlhášení.");
            });
        },
        authenticate : function(){
            $http({
                method: 'GET',
                url: 'api/user'
            }).then(function (resp){
                if(resp.data.authenticated === true){
                    auth.authenticated = true;
                    auth.username = resp.data.username;
                    auth.isAdmin = resp.data.isAdmin;
                }else{
                    auth.authenticated = false;
                    auth.username = null;
                    auth.isAdmin = false;
                    $location.path("/login").search({});
                }
            }, function (resp) {
                console.log("error", resp);
            });
        }
    };
    return auth;
}]).controller('LoginController',['AuthService', '$scope', '$location', '$routeParams', function(AuthService, $scope, $location, $routeParams){
    var self = this;
    self.login = function(){
        var credentials = {username: $scope.username, password: $scope.password};
        AuthService.login(credentials, function(error){
            if(error){
                $scope.errorMsg = error;
            }else {
                $scope.errorMsg = null;
                $location.path("/person/list");
            }
        });
    };
    //process logout
    if($routeParams.logout){
        if(AuthService.authenticated) {
            AuthService.logout(function(error){
                if(error){
                    $scope.errorMsg = error;
                }else {
                    $scope.errorMsg = null;
                    $scope.logoutMsg = "Uživatel úspěšně odhlášen.";
                }
            });
        }
    }
}]);