from PIL import Image
#from pylab import imshow, show, get_cmap
from numpy import random
from math import sqrt
import copy
from asteroid import Asteroid

def write_asteroid_to_image(img, asteroid):
  if asteroid.current_location[2] <= 0:
    print 'Asteroid is now behind us'
    return
  xmin = max(asteroid.current_location[0] - asteroid.current_radius, 0)
  xmax = min(asteroid.current_location[0] + asteroid.current_radius, img.size[0])
  ymin = max(asteroid.current_location[1] - asteroid.current_radius, 0)
  ymax = min(asteroid.current_location[1] + asteroid.current_radius, img.size[1])

  for x in range(xmin, xmax):
    for y in range(ymin, ymax):
      if sqrt((asteroid.current_location[0] - x)**2 + (asteroid.current_location[1] - y)**2) <= asteroid.current_radius:
        img.putpixel((x,y), random.rand() * 155 + 100)


# Start of Script!
image_size = (4000,4000)
# An asteroid is stored in the form [(x,y), radius, (x speed, y speed)]
asteroids = [Asteroid(image_size, (200,200,500), 500, (1,1,-1))] # [[(200, 200), 100, (1, 1)]]

# Randomly create a bunch of asteroids
for i in range(4):
  asteroids.append(Asteroid(image_size))

for asteroid in asteroids:
  print asteroid

# Creates random colors for full size
#image_data = 255 * random.random((image_size[0] * image_size[1], 3));

#image_data = random.random(image_size[0] * image_size[1]) * 155 + 100

# flatten so that it is a single array
#image_data = [item for sublist in image_data for item in sublist]

#imshow(image_data, cmap=get_cmap("Spectral"), interpolation='nearest')
#show()

#print image_data

for i in range(10):
  time = i * 30
  print 'TIME: {}'.format(time)

  img = Image.new('F', image_size)
  #img.putdata(image_data)

  for asteroid in asteroids:
    #new_asteroid = move_asteroid(asteroid, time)
    asteroid.move(time)
    print '<Asteroid #%d current(location: %s, size: %s radius: %s), trajectory: %s' % (
      asteroid.id, asteroid.current_location, asteroid.size, asteroid.current_radius, asteroid.trajectory)
    write_asteroid_to_image(img, asteroid)


  img = img.convert('RGB')
  #img.show()
  img.save('test_{}.png'.format(i))
