var express = require('express');
var fs = require('fs');
var router = express.Router();
var mysql = require('mysql');
var connection = mysql.createConnection({
    host     : 'localhost',
    user     : 'kyle',
    password : 'admin',
    database : 'qr_webserver'
});
connection.connect();


/* GET users listing. */
router.get('/', function(req, res, next) {
    res.render('register', { title: 'Register Here' });
});

router.post('/', function(req,res,next) {
    var post = {id: null, name: req.body.name, password: req.body.password};
    connection.query('INSERT INTO users SET ?', post, function (err, result) {
        if (err) {
            console.error(err);
            return;
        }
        console.error(result);
        res.render('index', {title: 'Thanks for Registering!'});

    });
});
module.exports = router;
