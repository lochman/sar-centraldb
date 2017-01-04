'use strict';
var App = angular.module('app', ['ngResource', 'ui.bootstrap', 'ngRoute', 'auth', 'navigation', 'person']);
App.config(function($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl : "home.html",
            controller: "HomeController",
            controllerAs: 'homeCtrl'
        })
        .when("/login", {
            templateUrl : "login.html",
            controller: "LoginController",
            controllerAs: 'loginCtrl'
        })
        .when("/person/list", {
            templateUrl : "person-list.html",
            controller: "PersonListController",
            controllerAs: 'personListCtrl'
        })
        .when("/person/new", {
            templateUrl : "person-edit.html",
            controller: "PersonEditController",
            controllerAs: 'personCtrl',
            resolve: {editPage: function(){return false;}}
        })
        .when("/person/edit/:id", {
            templateUrl : "person-edit.html",
            controller: "PersonEditController",
            controllerAs: 'personCtrl',
            resolve: {editPage: function(){return true;}}
        })
        .when("/person/:id", {
            templateUrl : "person.html",
            controller: "PersonController",
            controllerAs: 'personCtrl'
        })
        .otherwise({
            template: "<h1> Chyba:</h1><h2>Požadovaná akce není dostupná.</h2>"
        })
    ;
}).run(['AuthService', function(AuthService) {
    AuthService.authenticate();
}]).controller('HomeController', ['$scope', '$routeParams', function($scope, $routeParams) {
    if($routeParams.created){
        $scope.message = "Nově vytvořená osoba předána ke zpracování.";
    }
    if($routeParams.edited) {
        $scope.message = "Změněná osoba předána ke zpracování.";
    }
}]);