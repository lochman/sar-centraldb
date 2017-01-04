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