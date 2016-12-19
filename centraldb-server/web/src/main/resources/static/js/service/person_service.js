App.factory('Person', ['$resource', function ($resource) {
    //$resource() function returns an object of resource class
    return $resource(
        'person/:id',
        {id: '@id'},
        {
            update: {
                method: 'POST' // To send the HTTP Put request when calling this custom update method.
            }

        }
    );
}]);