const path = require('path');
const WrmPlugin = require('atlassian-webresource-webpack-plugin');

const PLUGIN_TARGET_DIR = path.join(__dirname, 'target');
const SRC_DIR = path.join(__dirname, 'src', 'main', 'typescript');
const OUTPUT_PATH = path.join(PLUGIN_TARGET_DIR, 'classes');

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
    },
    plugins: [
        new WrmPlugin({
            pluginKey: 'de.schroenser.timera.timera',
            xmlDescriptors: path.resolve(OUTPUT_PATH, 'META-INF', 'plugin-descriptors', 'wr-webpack-bundles.xml'),
            contextMap: {
                timera: ['atl.general']
            }
        })]
};

module.exports = webpackConfig;
