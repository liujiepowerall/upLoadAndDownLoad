var cordova = require('cordova'),
    exec = require('cordova/exec');

var uploadAndDownloadPlugin = function() {

};
uploadAndDownloadPlugin.prototype.upload = function(resourcePath)
{
	alert("upload");
    exec(null, null, 'UploadAndDownloadPlugin', 'upload', [resourcePath]);
};
uploadAndDownloadPlugin.prototype.download = function(downloadPath)
{
	alert("download");
    exec(null, null, 'UploadAndDownloadPlugin', 'download', [downloadPath]);
};
var upAndDown = new uploadAndDownloadPlugin();

module.exports = upAndDown;