var express = require('express');
var router = express.Router();
var Cookies = require('cookies');
var mysql = require('mysql');
var connection = mysql.createConnection({
  host     : 'localhost',
  user     : 'kyle',
  password : 'admin',
  database : 'qr_webserver'
});
connection.connect();

/* GET home page. */
router.get('/', function(req, res, next) {
  var cookies = new Cookies(req, res);
  cookies.set('login_cookie' , '1111111111111111', {expire : new Date() + 9999, overwrite: true});
  res.render('index', { title: 'QR Messaging server' });
});

router.post('/', function(req,res,next) {
  connection.query('SELECT * FROM users WHERE name = ? AND password = ?', [req.body.name, req.body.password], function (err, result) {
    if (err) {
      console.error(err);
      return;
    }
    console.error(result);
    if (result.length > 0) {
      res.render('chat', {title: 'User-Specific Chat History'});
    }
    else {
      res.render('index', { title: 'Login Failed' });
    }
  });
});
module.exports = router;
