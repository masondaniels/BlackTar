# Black Tar
Black Tar is a Java TeaVM library with WebGL bindings. Its primary purpose is to serve as an internal 3d and 2d engine for video game development for Attar Software LLC.

## Goals
| Goal | Implementation Status | Date Implemented |
|--------------|-----------|------------|
| The engine should have all GL 1 constants.|✅|1/26/2024|
| The engine should be able to render something 2d in GL.|✅|1/26/2024|
| The engine should be able to render something 3d in GL.|✅|2/4/2024|
| The engine should be able to draw complete 3d graphics. |❌| |
| The engine should be able to draw complete 2d graphics. |❌| |
| The engine should be able to draw modern UI which fits the 3d/2d graphics. |❌| |
| The engine should have a built in camera for looking at the environment in both 3d/2d. |❌| |
| The engine should have textures, shadows, etc. Nothing too complex. |❌| |

## Visual
![](https://i.imgur.com/FzbDDFd.gif)

The engine currently renders a triangle at (0, 0.5, 0), (-0.5, -0.5, 0), (0.5, -0.5, 0) rotated about the x and z axis by a factor of 2 and 1 respectively.

The camera is at position (0, 0, -2) facing the z axis head on.

The engine can effectively render anything in 3d space now. 