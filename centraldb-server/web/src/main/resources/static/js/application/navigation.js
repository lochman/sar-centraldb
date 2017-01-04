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
    self.logout = function() {
        $location.path("login").search({logout: true});
    }

}]);