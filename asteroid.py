from numpy import random

class Asteroid:
  #initial_position = (200,200)
  #size = 100
  #trajectory = (1,1)

  def __init__(self, loc_constraint, initial_location = None, size = None, trajectory = None):
    self.initial_location = initial_location or (random.randint(loc_constraint[0]), random.randint(loc_constraint[1]))
    self.size = size or random.randint(50, min([a / 4 for a in loc_constraint] + [500]))
    self.trajectory = trajectory or (random.randint(-10, 10), random.randint(-10, 10))
    self.hist_location = {0: self.initial_location}
    self.hist_radius = {0: self.size / 2}
    self.current_location = self.initial_location
    self.current_radius = self.size / 2

  def move(self, elapsed_seconds):
    self.current_radius = self.radius(elapsed_seconds)
    self.current_location = self.location(elapsed_seconds)

  def radius(self, elapsed_seconds):
    if not elapsed_seconds in self.hist_radius:
      self.hist_radius[elapsed_seconds] = self.size / 2
    return self.hist_radius[elapsed_seconds]

  def location(self, elapsed_seconds):
    if not elapsed_seconds in self.hist_location:
      self.hist_location[elapsed_seconds] = \
        (self.initial_location[0] + self.trajectory[0] * elapsed_seconds,
         self.initial_location[1] + self.trajectory[1] * elapsed_seconds)
    return self.hist_location[elapsed_seconds]

