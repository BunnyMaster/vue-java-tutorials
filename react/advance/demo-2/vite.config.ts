import tailwindcss from '@tailwindcss/vite';
import react from '@vitejs/plugin-react-swc';
import path from 'path';
import { defineConfig } from 'vite';
import babel from 'vite-plugin-babel';
import tsconfigPaths from 'vite-tsconfig-paths';

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),
    babel({
      babelConfig: {
        plugins: ['babel-plugin-react-compiler'],
      },
    }),
    tailwindcss(),
    tsconfigPaths(),
  ],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    port: 8848,
    open: true,
    host: '0.0.0.0',
  },
});
