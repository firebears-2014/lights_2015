# lights_2015

This program watches Network Tables for changes to the "lights" table.
For each PixelStrip we define on the robot, there will be a corresponding
entry in the Network Table.  Changing the text in that entry changes
the animation on the robot.

# params 0 is strip number 1-3
strip0 - animation [a]
strip0.value - progression
strip0.bg - background animation [a]
strip0.dim - 0-255 transparent -to- opaque background animation

# [a]
PULSING_GREEN_ANIM
MOVING_BLUE_ANIM
FIRE_ANIM
LIFT
CRAZY
