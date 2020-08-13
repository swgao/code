seajs.config({
    alias: {
        'jquery': 'jquery.min',
        '$': 'jquery.min',
        'jquery.migrate': 'jquery-migrate-1.2.1.min',
        'plugins': 'plugins',

            /* modules */
        'main': 'modules/main',
        'authc': 'modules/authc',
        'sidebox': 'modules/sidebox',
        'post': 'modules/post',
        'comment': 'modules/comment',
        'phiz': 'modules/phiz',
        'avatar': 'modules/avatar',
        'editor': 'modules/editor',
        'view': 'modules/view',
        'webuploader': 'modules/webuploader',

            /* lib */
        'bootstrap': 'lib/bootstrap/js/bootstrap.min',
        'baguetteBox': 'lib/baguette/baguetteBox.min',
        'layer': 'lib/layer/layer',
        'pace': 'lib/pace/pace.min',
        'pjax': 'lib/pjax/jquery.pjax',
        'dmuploader': 'lib/uploader/dmuploader',
        'webuploader.min': 'lib/webuploader/webuploader.min',
        'webuploader.css': 'lib/webuploader/webuploader.css',
        'jcrop': 'lib/jcrop/jquery.jcrop.min',
        'validate': 'lib/validate/jquery-validate',
        'lazyload': 'lib/lazyload/jquery.lazyload',

        'ueditor': 'lib/ueditor/ueditor.all.min',
        'ueditor.config': 'lib/ueditor/ueditor.config',
        'ueditor.parse': 'lib/ueditor/ueditor.parse.min'
    },

    // 预加载项
    preload: [this.JSON ? '' : 'json', 'jquery'],

        // 路径配置
    paths: {
        'lib': '../../lib',
    },

    // 变量配置
    vars: {
        'locale': 'zh-cn'
    },

    charset: 'utf-8',

    debug: false
});

var __SEAJS_FILE_VERSION = '?v=1.3';

//seajs.on('fetch', function(data) {
//	if (!data.uri) {
//		return ;
//	}
//
//	if (data.uri.indexOf(app.mainScript) > 0) {
//		return ;
//	}
//
//    if (/\:\/\/.*?\/assets\/libs\/[^(common)]/.test(data.uri)) {
//        return ;
//    }
//
//    data.requestUri = data.uri + __SEAJS_FILE_VERSION;
//
//});
//
//seajs.on('define', function(data) {
//	if (data.uri.lastIndexOf(__SEAJS_FILE_VERSION) > 0) {
//	    data.uri = data.uri.replace(__SEAJS_FILE_VERSION, '');
//	}
//});