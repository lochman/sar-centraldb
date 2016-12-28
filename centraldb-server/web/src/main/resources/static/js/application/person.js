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
        //convertor from czech date
        copy.birthDate = (new Date(copy.birthDate.replace( /(\d+)[^\d]+(\d+)[^\d]+(\d+)/, "$2/$1/$3") )).getTime();
        //convert true/false to 1/0
        copy.usePermitted = copy.usePermitted ? "1" : "0";
        console.log("datumek", copy.birthDate);
        var obj = {person: copy, addressWrappers: $scope.person.addressWrappers};
        if (editPage){
           /* $http({
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

*/


            Person.update(obj, function (resp){
                console.log(resp);
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
                console.log(resp);
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