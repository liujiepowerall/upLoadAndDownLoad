var cordova = require('cordova'),
    exec = require('cordova/exec');

var uploadAndDownLoadPlugin = function() {

};
uploadAndDownLoadPlugin.prototype.upload = function(resourcePath)
{
    exec(null, null, 'UpLoadAndDownLoadPlugin', 'upload', [resourcePath]);
};
uploadAndDownLoadPlugin.prototype.download = function(downloadPath)
{
    exec(null, null, 'UpLoadAndDownLoadPlugin', 'download', [downloadPath]);
};
var upAndDown = new uploadAndDownLoadPlugin();

module.exports = upAndDown;