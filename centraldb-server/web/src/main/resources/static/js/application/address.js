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