angular.module('address', ['ngResource']).component('addressEdit', {
    templateUrl: 'address-edit.html',
    controller: 'AddressEditController',
    controllerAs: 'addressCtrl',
    bindings: {
        address: '=',
        types: "<",
        index: '<',
        removeAddress: '&'
    }
}).controller('AddressEditController', ['$scope', function($scope) {
    var self = this;
    $scope.address = self.address;
    $scope.addressTypes = self.types;
    if (self.address.addressType){
        $scope.address.addresstype = String(self.address.addressType.id);
    }
}]).component('addressTable', {
    templateUrl: 'address.html',
    controller: 'AddressController',
    controllerAs: 'addressCtrl',
    bindings: {
        address: '<'
    }
}).controller('AddressController', ['$scope', function($scope) {
    var self = this;
    $scope.address = self.address;
}]);
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
angular.module('person', ['ngResource', 'auth', 'address'])
    .factory('Person', ['$resource', function ($resource) {
    return $resource(
        'api/person/:id',
        { id: '@id' },
        {
            update: {
                url: 'api/person/:id',
                method: 'PUT',
                params: {id: '@person.id'}
            },
            create: {
                url: 'api/person',
                method: 'POST'
            },
            paginatedSearch: {
                url: 'api/person/search/paginated',
                method: 'POST'
            }

        }
    );
}]).controller('PersonController', ['$scope', 'Person', 'AuthService', '$location', '$routeParams', function($scope, Person, AuthService, $location, $routeParams) {
    var self = this;
    self.id = $routeParams.id;
    $scope.id = self.id;
    self.person = Person.get({id: self.id}, function () {
        $scope.person = self.person;
        $scope.personParams = Object.keys(self.person);
        $scope.error = false;
    }, function () {
        $scope.error = true;
    });
    self.isAdmin = function () {
        return AuthService.isAdmin;
    };
    self.goEdit = function () {
        $location.path("person/edit/" + self.id);
    };
}]).controller('PersonEditController', ['$scope', '$http', '$filter', 'editPage', 'Person', '$location', '$routeParams', function($scope, $http, $filter, editPage, Person, $location, $routeParams) {
    var self = this;
    $scope.editPage = editPage;
    self.id = $routeParams.id;
    $scope.id = self.id;
    self.person = {};
    $scope.person = {};
    self.person.addressWrappers = [];
    $scope.person.addressWrappers = [];
    $scope.personTypes = $http({
        method: 'GET',
        url: 'api/person/types'
        }).then(function successCallback(response) {
            if(response.data) {
                $scope.personTypes = response.data;
            }
        }, function errorCallback() {
            console.log("Chyba při zpracování typu osoby.");
        });
    $scope.addressTypes = $http({
        method: 'GET',
        url: 'api/address/types'
        }).then(function successCallback(response) {
            if(response.data) {
                $scope.addressTypes = response.data;
            }
        }, function errorCallback() {
            console.log("Chyba při zpracování typu adresy.");
        });
    if (editPage) {
        self.person = Person.get({id: self.id},
            function () {
                $scope.person = self.person;
                $scope.persontype = String(self.person.personType.id);
                $scope.error = false;
                $scope.person.birthDate = $filter('date')(self.person.birthDate, 'd.M.yyyy');
            }, function () {
                $scope.error = true;
            });
    }
    self.submitPerson = function() {
        //convertor string id to personType
        for(var i in $scope.personTypes){
            if($scope.personTypes[i].id == $scope.persontype ) {
                $scope.person.personType = $scope.personTypes[i];
                break;
            }
        }
        //convertor string id to adressType
        for(var i in $scope.person.addressWrappers){
            for(var y in $scope.addressTypes){
                if ($scope.person.addressWrappers[i].addresstype == $scope.addressTypes[y].id){
                    $scope.person.addressWrappers[i].addressType = $scope.addressTypes[y];
                    delete $scope.person.addressWrappers[i].addresstype;
                    break;
                }
            }
        }
        //deep copy
        var copy = JSON.parse(JSON.stringify($scope.person));
        delete copy.addressWrappers;
        //convertor from czech date
        copy.birthDate = (new Date(copy.birthDate.replace( /(\d+)[^\d]+(\d+)[^\d]+(\d+)/, "$2/$1/$3") )).getTime();
        //convert true/false to 1/0
        copy.usePermitted = copy.usePermitted ? "1" : "0";
        var obj = {person: copy, addressWrappers: $scope.person.addressWrappers};
        if (editPage){
            Person.update(obj, function (resp){
                if(resp.status){
                    $location.path("").search("edited");
                }else {
                    console.log("Chyba pri zpracovani na serveru");
                }
            }, function(err) {
                console.log("Chyba pri zpracovani na serveru");
            });
        }else{
            Person.create(obj, function (resp){
                if(resp.status){
                    $location.path("").search("created");
                }else {
                    console.log("Chyba pri zpracovani na serveru");
                }
            }, function(err) {
                console.log("Chyba pri zpracovani na serveru");
            });
        }
    };
    self.addAddress = function (){
        $scope.person.addressWrappers.push({});
    };
    self.removeAddress = function (index){
        $scope.person.addressWrappers.splice(index,  1);
    }
}]).controller('PersonListController', ['$scope', '$http', 'Person', function($scope, $http, Person) {
    var self = this;
    self.persons = [];
    self.queryParams = {};
    self.paginationParams = {limit: 10, currentPage: 1, sort: null};
    $scope.paginationParams = {};
    $scope.paginationParams.currentPage = 0;
    $scope.paginationParams.limit = 10;
    $scope.paginationParams.totalCount = 0;
    $scope.paginationParams.maxSize = 5;
    $scope.personTypes = $http({
        method: 'GET',
        url: 'api/person/types'
    }).then(function successCallback(response) {
        if(response.data) {
            $scope.personTypes = response.data;
        }
    }, function errorCallback() {
        console.log("Chyba při zpracování typu osoby.");
    });
    self.changePaginationLimit = function(){
        self.paginationParams.limit = $scope.paginationParams.limit;
        self.paginatedSearch();
    };
    self.pageChanged = function() {
        self.paginationParams.currentPage = $scope.paginationParams.currentPage;
        self.paginatedSearch();
    };
    self.paginatedSearch = function() {
        if ($scope.name) {
            self.queryParams.name = $scope.name;
        }else{
            delete self.queryParams.name;
        }
        if ($scope.firstname) {
            self.queryParams.firstName = $scope.firstname;
        }else{
            delete self.queryParams.firstName;
        }
        if ($scope.socialnumber) {
            self.queryParams.socialNumber = $scope.socialnumber;
        }else{
            delete self.queryParams.socialNumber;
        }
        if ($scope.companynumber) {
            self.queryParams.companyNumber = $scope.companynumber;
        }else{
            delete self.queryParams.companyNumber;
        }
        if ($scope.gender) {
            self.queryParams.gender = $scope.gender;
        }else{
            delete self.queryParams.gender;
        }
        if ($scope.persontype) {
            self.queryParams.personType = $scope.persontype;
        }else{
            delete self.queryParams.personType;
        }

        var pp = {};
        pp.page = self.paginationParams.currentPage - 1;
        pp.limit = self.paginationParams.limit;
        pp.sort = self.paginationParams.sort;
        Person.paginatedSearch({queryParams: self.queryParams, paginationParams: pp},
            function(data) {
                self.paginationParams.totalCount = Number(data.totalElements);
                self.paginationParams.currentPage = Number(data.number) + 1;
                self.paginationParams.limit =  Number(data.size);
                //for select box need to be string
                $scope.paginationParams.limit = String(self.paginationParams.limit);
                $scope.paginationParams.currentPage = self.paginationParams.currentPage;
                $scope.paginationParams.totalCount = self.paginationParams.totalCount;
                if (Array.isArray(data.content)) {
                    self.persons = data.content;
                } else{
                    self.persons = [];
                }
                $scope.persons = self.persons;
            }, function(err){
                console.log("error", err);

            }
        );
    };
    //init
    self.paginatedSearch();
}]).directive('rowHref', ['$compile', function ($compile) {
    //anchor for row maker
    return{
        restrict: 'A',
        link: function (scope, element, attr) {
            var cells = element.children();
            cells.addClass('cell-link');
            for (var i = 0; i < cells.length; i++){
                var link = $('<a ng-href="'+ attr.rowHref +'"></a>');
                //compile and prelink
                ($compile(link))(scope);
                $(cells[i]).prepend(link);
            }
        }
    }
}]);
