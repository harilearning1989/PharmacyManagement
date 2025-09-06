const { app, BrowserWindow } = require('electron');
const path = require('path');

function createWindow() {
  const win = new BrowserWindow({
    width: 1424,
    height: 768,
    webPreferences: {
      nodeIntegration: false,  // better for security
      contextIsolation: true
    },
  });

  // Load Angular's index.html from dist
  const indexPath = path.join(__dirname, 'dist/pharmacy-mgmt-ui/index.html');
  win.loadFile(indexPath)
    .catch(err => console.error('Error loading index.html:', err));

  // Open dev tools for debugging
  //win.webContents.openDevTools();
}

app.whenReady().then(createWindow);

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit();
  }
});

app.on('activate', () => {
  if (BrowserWindow.getAllWindows().length === 0) {
    createWindow();
  }
});
