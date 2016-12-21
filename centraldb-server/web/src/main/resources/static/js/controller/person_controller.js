'use strict';
App.controller('PersonController', ['$scope', 'Person', '$window', function($scope, Person, $window) {
    var self = this;
    self.person = new Person();
    self.people = [];

    self.fetchAllPeople = function() {
        self.people = Person.query();
    };

    self.createPerson = function() {
        console.log("savecalled", self.person.name);
      /*  self.person.$save(function(resp) {
            console.log("saved", self.person.name, resp, self.person);
            self.$get();
        });*/
        self.person.$save(function(response, headers){
            self.person = response;
            console.log("save", self.person, response, headers('Location'));
            $window.location.href = headers('Location');
        });
        console.log("wtf",  self.person);
    };

    self.updatePerson = function() {
        self.person.$update(function() {
            self.fetchAllPeople();
        });
    };

    self.deletePerson = function(identity) {
        var person = Person.get({id:identity}, function() {
            person.$delete(function() {
                console.log('Deleting person with id ', identity);
                self.fetchAllPeople();
            });
        });
    };

    self.fetchAllPeople();

    self.submit = function() {
        self.person.name = $scope.name;
        console.log('Updating person with name', $scope.name);
        if (self.person.id == null) {
            console.log('Saving new person', self.person);
            self.createPerson();
        } else {
            console.log('Updating person with id ', self.person.id);
            self.updatePerson();
            console.log('Person updated with id ', self.person.id);
        }
        self.reset();
    };

    self.edit = function(id) {
        console.log('id to be edited', id);
        for (var i = 0; i < self.people.length; i++) {
            if(self.people[i].id === id) {
                self.person = angular.copy(self.people[i]);
                break;
            }
        }
    };

    self.remove = function(id) {
        console.log('id to be deleted', id);
        if (self.person.id === id) {//If it is the one shown on screen, reset screen
            self.reset();
        }
        self.deletePerson(id);
    };

    self.reset = function() {
        self.person= new Person();
        $scope.addPersonForm.$setPristine(); //reset Form
    };
}]);