angular.module('auth', []).factory( 'AuthService', ['$http', '$location', function($http, $location) {
    var auth = {
        authenticated : false,
        isAdmin: false,
        username: null,
        homePath : '/',
        login : function(credentials, callback) {
            console.log("login()", credentials.password, credentials.username);
            $http({
                method: 'POST',
                url: 'sec/login',
                data: 'username='+ credentials.username+'&password='+credentials.password,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then(function successCallback(response) {
                console.log("s", response);
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
            console.log("logout()");
            $http({
                method: 'POST',
                url: 'sec/logout',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then(function successCallback(response) {
                console.log("su", response);
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
                console.log("s", resp.data);
                if(resp.data.authenticated === true){
                    auth.authenticated = true;
                    auth.username = resp.data.username;
                    auth.isAdmin = resp.data.isAdmin;
                }else{
                    auth.authenticated = false;
                    auth.username = null;
                    auth.isAdmin = false;
                    console.log("unsigned");
                    $location.path("/login").search({});
                }
                // this callback will be called asynchronously
                // when the response is available
            }, function (resp) {
                console.log("e", resp);
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
        }
    };
    return auth;
}]).controller('LoginController',['AuthService', '$scope', '$location', '$routeParams', function(AuthService, $scope, $location, $routeParams){
    var self = this;
    console.log("init login cont");
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
    console.log($routeParams);
    if($routeParams.logout){
        console.log("logout z cntr");
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