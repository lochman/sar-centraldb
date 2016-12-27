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
    console.log("init address", self.address, self.index, self.types, self.wrapper);
    $scope.address = self.address;
    $scope.addressTypes = self.types;
    /*self.removeAddress = function(){
        console.log("remove shit", self.index, self.remove);
        self.remove(self.index);
    }*/
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
    var self  = this;
    console.log("running app");
    AuthService.authenticate();
}]).controller('HomeController', ['$scope', '$routeParams', function($scope, $routeParams) {
    var self = this;
    if($routeParams.created){
        $scope.message = "Nově vytvořená osoba předána ke zpracování.";
    }
    if($routeParams.edited) {
        $scope.message = "Změněná osoba předána ke zpracování.";
    }
}]);


/*
App.run( function($rootScope, $location) {
    // register listener to watch route changes
    $rootScope.$on( "$routeChangeStart", function(event, next, current) {
        if ( $rootScope.loggedUser == null ) {
            // no logged user, we should be going to #login
            if ( next.templateUrl == "partials/login.html" ) {
                // already going to #login, no redirect needed
            } else {
                // not going to #login, we should redirect now
                $location.path( "/login" );
            }
        }
    });
})*/
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
angular.module('person', ['ngResource', 'auth', 'address'])
    .factory('Person', ['$resource', function ($resource) {
    return $resource(
        'api/person/:id',
        { id: '@id' },
        {
            update: {
                method: 'PUT'
            },
            paginatedSearch: {
                url: 'person/search/paginated',
                method: 'POST'
            }

        }
    );
}]).controller('PersonController', ['$scope', 'Person', 'AuthService', '$location', '$routeParams', function($scope, Person, AuthService, $location, $routeParams) {
    var self = this;
    self.id = $routeParams.id;
    $scope.id = self.id;
    console.log("init", self.id);
    //console.log("init",[[${id}]]);
    self.person = Person.get({id: self.id}, function () {
        $scope.person = self.person;
        $scope.personParams = Object.keys(self.person);
        console.log("hmm", self.person);
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
}]).controller('PersonEditController', ['$scope', '$http', 'editPage', 'Person', '$location', '$routeParams', function($scope, $http, editPage, Person, $location, $routeParams) {
    var self = this;
    $scope.editPage = editPage;
    self.id = $routeParams.id;
    $scope.id = self.id;
    console.log("init", self.id);
    self.person = {};
    $scope.person = {};
    self.person.addressWrappers = [];
    $scope.person.addressWrappers = [];
    $scope.personTypes = $http({
        method: 'GET',
        url: 'api/person/types'
        }).then(function successCallback(response) {
            console.log("s", response);
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
            console.log("s", response);
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
                console.log("hmm", self.person, $scope);
                $scope.error = false;
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
        console.log('wtf', $scope.person.addressWrappers);
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
        console.log("send pers", $scope.person, $scope);
        //deep copy
        var copy = JSON.parse(JSON.stringify($scope.person));
        delete copy.addressWrappers;
        var obj = {person: copy, addressWrappers: $scope.person.addressWrappers};
        if (editPage){
            $http({
                method: 'PUT',
                url: 'api/person/'+self.id,
                data: obj,
                headers: {'Content-Type': 'application/json'}
            }).then(function (resp){
                console.log(resp);
                if(resp.status){
                    $location.path("").search("edited");
                }
                console.log("Chyba pri zpracovani na serveru");
            }, function(err) {
                console.log("Chyba pri zpracovani na serveru");
            });



            /*
            Person.update(obj, function (resp){
                console.log(resp);
                if(resp.status){
                    $location.path("").search("edited");
                }
                console.log("Chyba pri zpracovani na serveru");
            }, function(err) {
                console.log("Chyba pri zpracovani na serveru");
            });*/
        }else{
            Person.$save(obj, function (resp){
                console.log(resp);
                if(resp.status){
                    $location.path("").search("created");
                }
                console.log("Chyba pri zpracovani na serveru");
            }, function(err) {
                console.log("Chyba pri zpracovani na serveru");
            });
        }
    };
    self.addAddress = function (){
        console.log("pridavam");
        $scope.person.addressWrappers.push({});
        console.log('add', $scope.addressWrappers);
        //$scope.$apply();
    };
    self.removeAddress = function (index){
        console.log("odebiram ", index);
        $scope.person.addressWrappers.splice(index,  1);
        //$scope.$apply();
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
    console.log("nechapu s");
    $scope.personTypes = $http({
        method: 'GET',
        url: 'api/person/types'
    }).then(function successCallback(response) {
        console.log("s", response);
        if(response.data) {
            $scope.personTypes = response.data;
        }
    }, function errorCallback() {
        console.log("Chyba při zpracování typu osoby.");
    });
    self.changePaginationLimit = function(){
        self.paginationParams.limit = $scope.paginationParams.limit;
        self.paginatedSearch();
        console.log(self.paginationParams)
    };
    self.pageChanged = function() {
        self.paginationParams.currentPage = $scope.paginationParams.currentPage;
        console.log("stranka na ", $scope.paginationParams.currentPage, $scope.paginationParams.limit);
        self.paginatedSearch();
    };
    self.paginatedSearch = function() {
        console.log(2);
        console.log("serch called", $scope);
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
        console.log("serchh called", JSON.stringify({queryParams: self.queryParams, paginationParams: pp}));
        Person.paginatedSearch({queryParams: self.queryParams, paginationParams: pp},
            function(data) {
                console.log("done", data);
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
