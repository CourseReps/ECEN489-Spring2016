/**
 * Created by Kyle on 5/2/2016.
 */

var express = require('express');
var router = express.Router();
var fs = require('fs');
var busboy = require('connect-busboy');
var Cookies = require('cookies');

router.use(busboy());

/* GET home page. */
router.post('/', function(req, res, next) {
    var fstream;
    req.pipe(req.busboy);
    req.busboy.on('file', function (fieldname, file, filename) {
        console.log("Uploading: " + filename);
        fstream = fs.createWriteStream('c:/Users/Kyle/IdeaProjects/ECEN489_Project3/public/images/' + filename);
        file.pipe(fstream);
        fstream.on('close', function () {
            res.render('uploads', { title: 'Uploaded File' });
        });
    });
});

router.get('/', function(req, res, next) {
    var cookies = new Cookies(req, res);
    if(cookies.get('login_cookie')!=null){
        res.render('uploads', { title: 'Upload Here' });
    }
    else {
        res.render('index', { title: 'Something went wrong.' });
    }
});


module.exports = router;