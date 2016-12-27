angular.module('navigation', ['auth']).controller('NavigationController',['AuthService', '$location', function(AuthService, $location) {
    var self = this;
    self.authenticated = function() {
        return AuthService.authenticated;
    };
    self.username = function () {
        return AuthService.username;
    };

    self.isAdmin = function () {
        return AuthService.isAdmin;
    };
    /*
    self.login = function() {
        auth.authenticate(self.credentials, function(authenticated) {
            if (authenticated) {
                console.log("Login succeeded")
                self.error = false;
            } else {
                console.log("Login failed")
                self.error = true;
            }
        })
    };
    */
    self.logout = function() {
        console.log("logout");
        $location.path("login").search({logout: true});
        /*auth.logout(function(error){

        });*/
    }

}]);