import numpy as np
import matplotlib.pyplot as plt
from scipy import optimize as opt

data = np.transpose(np.loadtxt("timess.tpt"))
x = data[0]
y = data[1] #data[1,::2]

log_y = np.log(y)

# Exponential fitting
e_fit = np.polyfit(x, log_y, 1)

print("Exponential approximation:")
print(e_fit)

e_fct = np.exp(e_fit[1]) * np.exp(e_fit[0]*x)

# Polynomial fitting
def f1(x, b, a):
    return abs(a) + abs(b)*x
def f2(x, c, b, a):
    return abs(a) + abs(b)*x + abs(c)*x**2
def f3(x, d, c, b, a):
    return abs(a) + abs(b)*x + abs(c)*x**2 + abs(d)*x**3
def f4(x, e, d, c, b, a):
    return abs(a) + abs(b)*x + abs(c)*x**2 + abs(d)*x**3 + abs(e)*x**4

# Positive coefficients
coefficients1 = np.abs(np.append(opt.curve_fit(f1, x, y,maxfev=1000000)[0], []))
coefficients2 = np.abs(np.append(opt.curve_fit(f2, x, y,maxfev=1000000)[0], []))
coefficients3 = np.abs(np.append(opt.curve_fit(f3, x, y,maxfev=1000000)[0], []))
coefficients4 = np.abs(np.append(opt.curve_fit(f4, x, y,maxfev=1000000)[0], []))
# Postivie coefficients, based on 1-200
coefficients22 = np.abs(np.append(opt.curve_fit(f2, x[:200], y[:200],maxfev=1000000)[0], []))
coefficients32 = np.abs(np.append(opt.curve_fit(f3, x[:200], y[:200],maxfev=1000000)[0], []))
# All coefficients, based on 1-200
#coefficients43 = np.polyfit(x[:200], y[:200], 4)

print("Polynomial approximations:")
print(*coefficients1)
print(*coefficients2)
print(*coefficients3)
print(*coefficients4)
print("200 first value based:")
print(*coefficients22)
print(*coefficients22)
print("200 first value based, non-non-negative:")
#print(*coefficients43)

poly1 = np.poly1d(coefficients1)
poly2 = np.poly1d(coefficients2)
poly3 = np.poly1d(coefficients3)
poly4 = np.poly1d(coefficients4)
poly22 = np.poly1d(coefficients22)
poly32 = np.poly1d(coefficients32)
#poly43 = np.poly1d(coefficients42)

new_x = x #np.linspace(x[0], x[-1])
new_y1 = poly1(new_x)
new_y2 = poly2(new_x)
new_y3 = poly3(new_x)
new_y4 = poly4(new_x)
new_y22 = poly22(new_x)
new_y32 = poly32(new_x)
#new_y43 = poly43(new_x)

# Least squares
print("Least squares:")
print(np.sum((y-e_fct)**2))
print(np.sum((y-new_y1)**2))
print(np.sum((y-new_y2)**2))
print(np.sum((y-new_y3)**2))
print(np.sum((y-new_y4)**2))
print(np.sum((y-new_y22)**2))
print(np.sum((y-new_y32)**2))
#print(np.sum((y-new_y43)**2))

plt.plot(figsize=(40,30))
plt.plot(x, y, "o")
plt.plot(new_x, new_y1, new_x, new_y2, new_x, new_y3, new_x, new_y4)
#plt.plot(new_x, new_y22, new_x, new_y32)
#plt.plot(new_x, new_y43)
#plt.plot(x, e_fct)

# Write function values in file
out = np.transpose([x,y/1000,e_fct/1000,new_y1/1000,new_y2/1000,new_y3/1000,new_y4/1000,new_y22/1000,new_y32/1000])
f = open("functionvaluess.csv", "w")
f.write("n t exp p1 p2 p3 p4 p22 p32 \n")
for x in out:
    for y in x:
        f.write(str(y))
        f.write(" ")
    f.write("\n")
f.close()

plt.savefig("graphss.jpg")
