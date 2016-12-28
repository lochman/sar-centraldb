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
    console.log("init address", self.address, self.index, self.types);
    $scope.address = self.address;
    $scope.addressTypes = self.types;
    if (self.address.addressType){
        $scope.address.addresstype = String(self.address.addressType.id);
    }
    /*self.removeAddress = function(){
        console.log("remove shit", self.index, self.remove);
        self.remove(self.index);
    }*/
}]).component('addressTable', {
    templateUrl: 'address.html',
    controller: 'AddressController',
    controllerAs: 'addressCtrl',
    bindings: {
        address: '<'
    }
}).controller('AddressController', ['$scope', function($scope) {
    var self = this;
    console.log("init address", self.address);
    $scope.address = self.address;
}]);