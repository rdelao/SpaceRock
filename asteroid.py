from numpy import random

class Asteroid:
  next_id = 0
  size_range = (500, 4000)
  distance_range = (10, 500)
  speed_range = (-10,10)

  def __init__(self, loc_constraint, initial_location = None, size = None, trajectory = None):
    self.id = self.__class__.next_id + 1
    self.__class__.next_id += 1
    self.initial_location = initial_location or (random.randint(loc_constraint[0]), random.randint(loc_constraint[1]), random.randint(*self.distance_range))
    self.size = size or random.randint(*self.size_range) #50, min([a / 4 for a in loc_constraint] + [500]))
    self.trajectory = trajectory or tuple(random.randint(*self.speed_range, size=3))
    self.hist_location = {0: self.initial_location}
    self.hist_radius = {0: self.size / 2}
    self.current_location = self.initial_location
    self.current_radius = self.size / 2

  def move(self, elapsed_seconds):
    self.current_location = self.location(elapsed_seconds)
    self.current_radius = self.radius(elapsed_seconds)

  def radius(self, elapsed_seconds):
    if not elapsed_seconds in self.hist_radius:
      self.hist_radius[elapsed_seconds] = self.size / self.location(elapsed_seconds)[2] / 2
    return self.hist_radius[elapsed_seconds]

  def location(self, elapsed_seconds):
    if not elapsed_seconds in self.hist_location:
      self.hist_location[elapsed_seconds] = \
        (self.initial_location[0] + self.trajectory[0] * elapsed_seconds,
         self.initial_location[1] + self.trajectory[1] * elapsed_seconds,
         self.initial_location[2] + self.trajectory[2] * elapsed_seconds)
    return self.hist_location[elapsed_seconds]

  def __repr__(self):
    return '<%s #%d initial_location: %s, size: %s, trajectory: %s >' % (
        self.__class__.__name__, self.id, self.initial_location, self.size,
        self.trajectory)
