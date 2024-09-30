import React, { useEffect } from 'react';
import { NativeModules, Button } from 'react-native';

const { OverlayModule } = NativeModules;

const App = () => {

  useEffect(() => {
    const checkOverlayPermission = async () => {
      const canDraw = await OverlayModule.canDrawOverlays();
      if (!canDraw) {
        OverlayModule.requestOverlayPermission();
      } else {
        // Delay starting the overlay service by 6 seconds (6000 milliseconds)
        OverlayModule.startOverlayService();
      }
    };

    checkOverlayPermission();
  }, []);

  const startService = () => {
    // Delay starting the overlay service by 6 seconds (6000 milliseconds)
    OverlayModule.startOverlayService();
  };

  return (
    <Button title="Start Overlay Service" onPress={startService} />
  );
};

export default App;
