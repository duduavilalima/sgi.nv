const {src, dest, series} = require('gulp');
var clean = require('gulp-clean');
var gulpCopy = require('gulp-copy');
var sass = require('gulp-sass');
var fs = require('fs');
var lib = '../static/public/lib/';

function clean_lib() {
	console.log('Limpando pasta lib...');
	if (!fs.existsSync(lib)){
        fs.mkdirSync(lib);
    }
	return src('../static/public/lib/', {read: false})
	.pipe(clean({read: false, force: true, allowEmpty: true}));
}

function copy_files_fuses() {
	console.log('Copiando Fuse...');
	return src(['design/fuse/**', 'design/fuse/*.*'])
		.pipe(gulpCopy('../static/public/lib/fuse/', { prefix: 2, allowEmpty: true }));
}

function copy_files_datetimepicker() {
	console.log('Copiando Datetimepicker...');
	return src(['design/datetimepicker/**', 'design/datetimepicker/*.*'])
		.pipe(gulpCopy('../static/public/lib/datetimepicker', { prefix: 2, allowEmpty: true }));
}

function copy_files_picklist() {
	console.log('Copiando Picklist...');
	return src(['design/picklist/**', 'design/picklist/*.*'])
		.pipe(gulpCopy('../static/public/lib/picklist', { prefix: 2, allowEmpty: true }));
}

function copy_files_bower_components() {
	console.log('Copiando bower_components...');
	return src(['node_modules/@bower_components/**', 'node_modules/@bower_components/*.*'])
		.pipe(gulpCopy('../static/public/lib/', { prefix: 2, allowEmpty: true }));
}

function sass_style() {
	return src('scss/main.scss')
		.pipe(sass().on('error', sass.logError))
		.pipe(dest('../static/public/lib/style'));
}

exports.clean = clean_lib;
exports.copy_fuse = copy_files_fuses;
exports.copy_datetimepicker = copy_files_datetimepicker;
exports.copy_picklist = copy_files_picklist;
exports.copy_bower_components = copy_files_bower_components;
exports.sass = sass_style;
exports.default = series(
	clean_lib,
	series(
		copy_files_fuses,
		copy_files_datetimepicker,
		copy_files_picklist,
		copy_files_bower_components,
		sass_style
	)
);
