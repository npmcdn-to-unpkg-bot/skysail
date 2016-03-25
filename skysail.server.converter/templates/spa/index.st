index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en">

<head>
  <title>Stock Tracker</title>
  <meta charset="utf-8">
  <base href="/demoapp/v1/">
  <script>baseElement = document.querySelector('base');baseElement.attr = baseElement.getAttribute;</script>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="description" content="Angular2">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://storage.googleapis.com/code.getmdl.io/1.0.5/material.indigo-orange.min.css">
  <link rel="stylesheet" href="http://fonts.googleapis.com/icon?family=Material+Icons">
  <link rel="stylesheet" href="client/app.css">
</head>

<body style="background: #f3f3f3">

  <app></app>

  <!-- SystemJS -->
  <script src="static/lib/system.src.js"></script>
  <script src="static/lib/angular2.dev.js"></script>
  <script src="static/lib/http.dev.js"></script>
  <script src="static/lib/router.dev.js"></script>

  <!-- App -->
  <script>
  System.config({
    packages: {'client': {defaultExtension: 'js'}}
  });
  System.import('static/client/bootstrap');
  </script>
</body>

</html>

>>