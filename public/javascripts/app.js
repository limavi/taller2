var app = angular.module('app', ['ngAnimate','ngSanitize']);

app.config(['$provide', '$httpProvider', function ($provide, $httpProvider) {
  $provide.factory('AuthenticationInterceptor', ['$q', '$rootScope', function ($q, $rootScope) {
    console.log("entro al interceptor")
    return {
      'request': function (config) {
        var token = JWT.get();
        if (token) {
          config.headers['Authorization'] = token;
        }
        //console.log("OK interceptor request, config:  " + JSON.stringify(config))
        return config;
      },

      'responseError': function (rejection) {
        console.log("entro al interceptor responseError")
        if (rejection.status === 401) { // User isn't authenticated
          $rootScope.$emit('notification', 'warning', ' No tiene acceso a esta opción.');
          //$rootScope.logout();
        } else if (rejection.status === 403) { // User is authenticated but do not have permission to access this API

          $rootScope.$emit('notification', 'warning', 'No tiene acceso a esta opción');
        }
        //console.log("ERROR interceptor rejection:  " + JSON.stringify(rejection))
        return $q.reject(rejection);
      }
    }
  }]);
  // Add the interceptor
  $httpProvider.interceptors.push('AuthenticationInterceptor');
}]);

// Just some stuff to display notifications and the current user
app.run(['$rootScope', 'Authenticated', function ($rootScope, Authenticated) {
  $rootScope.$on('notification', function (e, severity, message) {
    $rootScope.notification = {severity: severity, message: message};
  });

  $rootScope.closeNotification = function closeNotification() {
    $rootScope.notification = null;
  };

  $rootScope.user = Authenticated.current
  $rootScope.logout = Authenticated.logout
}]);

// A singleton service to handle all authentification process
app.factory('Authenticated', ['$http', '$rootScope', function ($http, $rootScope) {
  var user = null;
  sync();

  // Function to read the token from the storage
  // and if present, assign it as the current authenticated user
  function sync() {
    var session = JWT.remember();
    user = session && session.claim && session.claim.user;
    $rootScope.authenticated = !!user;
  }

  function login(data) {
    console.log("realiza el login " + JSON.stringify(data))
    return $http.post('/api/login', data).then(function (response) {
      // If successful, read the new token from the header
      var token = response.headers("Authorization");
      var session = JWT.read(token);
      if (JWT.validate(session)) {
        JWT.keep(token); // Almacena el token en el Local storage
        sync();
      } else {
        logout();
      }
    });
  }

  function logout() {
    JWT.forget();
    sync();
  }

  // Test if a user is currently authenticated
  function isAuthenticated() {
    return !!user;
  }

  // Return the current user
  function current() {
    return user;
  }

  return {
    login: login,
    logout: logout,
    isAuthenticated: isAuthenticated,
    current: current
  };
}]);

// Simple controller to make some dummy HTTP request to our server
// and see if the user could actually do them or not
app.controller('HomeCtrl', ['$scope', '$http', 'Authenticated', function ($scope, $http, Authenticated) {
  var ctrl = this;

  ctrl.loginForm = {};

  ctrl.login = function login() {
    Authenticated.login(ctrl.loginForm).then(function () {
      ctrl.loginForm = {};
    }, function (error) {
      ctrl.notif('error', 'Invalid username or password!');
    });
  };

  ctrl.cancelarCreacionTramite = function cancelarCreacionTramite(){
    ctrl.jsonTramite="";
  };

  ctrl.PreVisualizarTramite = function PreVisualizarTramite(jsonTramite){
        //console.log("PreVisualizarTramite:  " + jsonTramite)
        return $http.get("/api/generarHtmlTramite?jsonConfTramite="+jsonTramite).then(function (response) {
          ctrl.paginaDelTramite =response.data;
          ctrl.notif('success', response.data);

          ctrl.myHTML =
                 'I am an <code>HTML</code>string with ' +
                 '<a href="#">links!</a> and other <em>stuff</em>';

        }, function (error) {
          ctrl.notif('error', error);
          // 401 and 403 errors are already handled by the interceptor
        });
  };

  function get1(endpoint) {
    return $http.get(endpoint).then(function (response) {
      ctrl.notif('success', response.data);
    }, function (error) {
      // 401 and 403 errors are already handled by the interceptor
    });
  }

  ctrl.notif = function notif(severity, message) {
    $scope.$emit('notification', severity, message);
  };
}]);
