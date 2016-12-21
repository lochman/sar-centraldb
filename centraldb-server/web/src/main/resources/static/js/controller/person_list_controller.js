'use strict';
App.controller('PersonListController', ['$scope', 'Person', function($scope, Person) {
    var self = this;
    self.allPersons = [];
    self.persons = [];
    self.queryParams = {};
    self.paginationParams = {limit: 50, currentPage: 1, sort: null};

    self.changePaginationLimit = function(){
        self.paginationParams.limit = $scope.paginationParams.limit;
        self.persons = self.allPersons.slice(0,self.paginationParams.limit);
        self.paginationParams.pageCount = Math.ceil(self.paginationParams.totalCount/self.paginationParams.limit);
        if(self.paginationParams.limit > self.allPersons.length){
            self.paginatedSearch();
        }
        console.log(self.paginationParams)
    };
    self.paginationChangePage = function(){
        self.paginationParams.currentPage = $scope.paginationParams.currentPage;
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

        console.log("serchh called", JSON.stringify({queryParams: self.queryParams, paginationParams: self.paginationParams}));
        var pp = {};
        pp.page = self.paginationParams.currentPage - 1;
        pp.limit = self.paginationParams.limit;
        pp.sort = self.paginationParams.sort;
        Person.paginatedSearch({queryParams: self.queryParams, paginationParams: pp},
            function(data) {
                console.log("done", data);
                self.paginationParams.totalCount = Number(data.totalElements);
                self.paginationParams.currentPage = Number(data.number) + 1;
                self.paginationParams.pageCount = Math.ceil(self.paginationParams.totalCount / self.paginationParams.limit);
                $scope.paginationParams.currentPage = self.paginationParams.currentPage;
                $scope.paginationParams.pageCount = self.paginationParams.pageCount;
                $scope.paginationParams.totalCount = self.paginationParams.totalCount;
                if (Array.isArray(data.content)) {
                    self.allPersons = data.content;
                } else{
                    self.allPersons = [];
                }
                self.persons = self.allPersons.slice(0,self.paginationParams.limit);
                $scope.persons = self.persons;
            }, function(err){
                console.log("error", err);
                self.paginationParams.totalCount = Number(555);
                self.paginationParams.currentPage = Number(5)+1;
                self.paginationParams.pageCount = Math.ceil(Number(555)/self.paginationParams.limit);
                $scope.paginationParams.currentPage = self.paginationParams.currentPage;
                $scope.paginationParams.pageCount = self.paginationParams.pageCount;
                $scope.paginationParams.totalCount = self.paginationParams.totalCount;
                self.allPersons = [{id: "asd", name:"dobricka", firstname: "petr", gender: "male", rc:"891020/xxxx", persontype:"fyzicka"}];
                self.persons = self.allPersons.slice(0,self.paginationParams.limit);
                $scope.persons = self.persons;
                console.log("done" , self.persons);
            }
        );
    };
    //init
    self.paginatedSearch();
}]);
//anchor for row maker
App.directive('rowHref', ['$compile', function ($compile) {
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