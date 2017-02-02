from PIL import Image
#from pylab import imshow, show, get_cmap
from numpy import random
from math import sqrt
import copy
from asteroid import Asteroid

def write_asteroid_to_image(img, asteroid):
  #xmin = max(asteroid[0][0] - asteroid[1], 0)
  #xmax = min(asteroid[0][0] + asteroid[1], img.size[0])
  #ymin = max(asteroid[0][1] - asteroid[1], 0)
  #ymax = min(asteroid[0][1] + asteroid[1], img.size[1])

  #for x in range(xmin, xmax):
  #  for y in range(ymin, ymax):
  #    if sqrt((asteroid[0][0] - x)**2 + (asteroid[0][1] - y)**2) <= asteroid[1]:
  #      img.putpixel((x,y), random.rand() * 155 + 100)
  xmin = max(asteroid.current_location[0] - asteroid.current_radius, 0)
  xmax = min(asteroid.current_location[0] + asteroid.current_radius, img.size[0])
  ymin = max(asteroid.current_location[1] - asteroid.current_radius, 0)
  ymax = min(asteroid.current_location[1] + asteroid.current_radius, img.size[1])

  for x in range(xmin, xmax):
    for y in range(ymin, ymax):
      if sqrt((asteroid.current_location[0] - x)**2 + (asteroid.current_location[1] - y)**2) <= asteroid.current_radius:
        img.putpixel((x,y), random.rand() * 155 + 100)

#def move_asteroid(asteroid, elapsed_seconds):
#  new_asteroid = copy.copy(asteroid)
#  new_asteroid[0] = (asteroid[0][0] + asteroid[2][0] * elapsed_seconds,
#                     asteroid[0][1] + asteroid[2][1] * elapsed_seconds)
#  return new_asteroid
#
#def generate_asteroid():
#  location = (random.randint(image_size[0]), random.randint(image_size[1]))
#  size = random.randint(50, min([a / 4 for a in image_size] + [500]))
#  trajectory = (random.randint(-10, 10), random.randint(-10, 10))
#  return [location, size, trajectory]

image_size = (4000,4000)
# An asteroid is stored in the form [(x,y), radius, (x speed, y speed)]
asteroids = [Asteroid(image_size, (200,200), 200, (1,1))] # [[(200, 200), 100, (1, 1)]]

# Randomly create a bunch of asteroids
for i in range(4):
  asteroids.append(Asteroid(image_size))

print asteroids

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

  img = Image.new('F', image_size)
  #img.putdata(image_data)

  for asteroid in asteroids:
    #new_asteroid = move_asteroid(asteroid, time)
    asteroid.move(time)
    write_asteroid_to_image(img, asteroid)


  img = img.convert('RGB')
  #img.show()
  img.save('test_{}.png'.format(i))
