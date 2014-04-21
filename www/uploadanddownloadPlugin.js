var cordova = require('cordova'),
    exec = require('cordova/exec');

var uploadAndDownloadPlugin = function() {

};
uploadAndDownloadPlugin.prototype.upload = function(resourcePath)
{
    exec(null, null, 'UploadAndDownloadPlugin', 'upload', [resourcePath]);
};
uploadAndDownloadPlugin.prototype.download = function(downloadPath)
{
    exec(null, null, 'UploadAndDownloadPlugin', 'download', [downloadPath]);
};
var upAndDown = new uploadAndDownloadPlugin();

module.exports = upAndDown;