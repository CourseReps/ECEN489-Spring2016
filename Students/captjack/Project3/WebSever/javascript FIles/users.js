var express = require('express');
var router = express.Router();
var Cookies = require('cookies');

/* GET users listing. */
router.get('/', function(req, res, next) {
  var cookies = new Cookies(req, res);
  if(cookies.get('login_cookie')!=null){
    res.render('users', { title: 'List of Authenticated Users' });
  }
  else {
    res.render('index', { title: 'Something went wrong.' });
  }
});

module.exports = router;
