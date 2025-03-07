const path = require('path');
const {merge} = require('webpack-merge');
const webpack = require('webpack');
const os = require('os');
const WRMPlugin = require('atlassian-webresource-webpack-plugin');

const PLUGIN_TARGET_DIR = path.join(__dirname, 'target');
const SRC_DIR = path.join(__dirname, 'src', 'main', 'typescript');
const OUTPUT_PATH = path.join(PLUGIN_TARGET_DIR, 'classes');

const getWrmPlugin = (watch = false, watchPrepare = false) => {
    return new WRMPlugin({
        pluginKey: 'de.schroenser.timera.timera',
        xmlDescriptors: path.resolve(OUTPUT_PATH, 'META-INF', 'plugin-descriptors', 'wr-webpack-bundles.xml'),
        watch: watch,
        watchPrepare: watchPrepare
    });
};

const webpackConfig = {
    mode: 'development',
    entry: {
        timera: path.join(SRC_DIR, 'index.tsx')
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            }, {
                test: /\.tsx?$/,
                use: 'ts-loader',
                exclude: /node_modules/
            }]
    },
    output: {
        path: OUTPUT_PATH,
        filename: '[name].[chunkhash].js'
    },
    resolve: {
        extensions: ['.tsx', '.ts', '.js']
    }
};

const hostname = 'localhost';
const devServerPort = '3333';

const watchPrepareConfig = {
    output: {
        publicPath: `http://${hostname}:${devServerPort}/`,
        filename: '[name].js'
    },
    plugins: [
        getWrmPlugin(true, true)]
};

const watchConfig = {
    output: {
        publicPath: `http://${hostname}:${devServerPort}/`,
        filename: '[name].js'
    },
    devServer: {
        host: hostname,
        port: devServerPort,
        hot: true,
        headers: {'Access-Control-Allow-Origin': '*'}
    },
    plugins: [
        getWrmPlugin(true)]
};

const devConfig = {
    plugins: [
        getWrmPlugin()]
};

module.exports = (env) => {
    if (env['watch:prepare']) {
        const mergedWebpackConfig = merge(webpackConfig, watchPrepareConfig);
        return mergedWebpackConfig;
    }

    if (env['watch']) {
        const mergedWebpackConfig = merge(webpackConfig, watchConfig);
        return mergedWebpackConfig;
    }

    const mergedWebpackConfig = merge(webpackConfig, devConfig);
    return mergedWebpackConfig;
};
