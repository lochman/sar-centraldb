App.factory('Person', ['$resource', function ($resource) {
    //$resource() function returns an object of resource class
    return $resource(
        'person/:id',
        {id: '@id'},
        {
            update: {
                method: 'POST'
            },
            paginatedSearch: {
                url: 'person/search/paginated',
                method: 'POST'
            }

        }
    );
}]);