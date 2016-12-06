App.factory('Person', ['$resource', function ($resource) {
    //$resource() function returns an object of resource class
    return $resource(
        'http://localhost:8080/centraldb/person',
        //{id: '@id'},
        {
            update: {
                method: 'POST' // To send the HTTP Put request when calling this custom update method.
            }

        }
    );
}]);