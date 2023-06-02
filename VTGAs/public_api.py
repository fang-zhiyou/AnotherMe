# Web Key:     5db52ac70099e993a576a6eed397f278
# JS web key:  bc98a73d5aa6d8e50441c78dcc5bb234

# Geo Point Format = [lng, lat]
import requests


# ---------------------------- Gaode API -------------------------------
params_convert = {
    'key': '5db52ac70099e993a576a6eed397f278',
    'locations': '-1,-1',
    'coordsys': 'gps',
    'output': 'json'
}
url_convert = "https://restapi.amap.com/v3/assistant/coordinate/convert?"


def get_gaode_loc(loc):
    """
    将GPS坐标 转换为 高德地图坐标
    :param loc: [lng, lat]
    :return: [lng, lat]
    """
    params_convert['locations'] = str(loc[0]) + "," + str(loc[1])

    response = requests.get(url=url_convert, params=params_convert)
    list_ = response.json()['locations'].split(';')
    points = []
    for v in list_:
        v_ = v.split(',')
        lng = round(float(v_[0]), 6)
        lat = round(float(v_[1]), 6)
        points.append([lng, lat])
    # print(points)
    return points


params_walking = {
    'key': '5db52ac70099e993a576a6eed397f278',
    'origin': '0,-1',
    'destination': '0,-1',
    'output': 'json',
    'alternative_route': '2',
    'show_fields': 'polyline'  # option
}
url_walking = "https://restapi.amap.com/v5/direction/walking?"

params_bicycling = {
    'key': '5db52ac70099e993a576a6eed397f278',
    'origin': '0,-1',
    'destination': '0,-1',
    'output': 'json',
    'alternative_route': '2',
    'show_fields': 'polyline'  # option
}
url_bicycling = "https://restapi.amap.com/v5/direction/bicycling?"

params_driving = {
    'key': '5db52ac70099e993a576a6eed397f278',
    'origin': '0,-1',
    'destination': '0,-1',
    'output': 'json',
    'alternative_route': '2',
    'show_fields': 'polyline'  # option
}
url_driving = "https://restapi.amap.com/v5/direction/driving?"


# input: start point, end point
def gen_traj_walking(st, en):
    """
    计算以步行的方式从 st 到 en 位置的导航轨迹
    :param st: [lng, lat]
    :param en: [lng, lat]
    :return: [[lng_1, lat_1],[lng_2, lat_2], ..., [lng_n, lat_n]]
    """
    ori = str(st[0]) + ',' + str(st[1])
    des = str(en[0]) + ',' + str(en[1])
    params_walking['origin'] = ori
    params_walking['destination'] = des

    response = requests.get(url=url_walking, params=params_walking)
    result = response.json()
    if result['status'] == '0':
        print("\033[91m" + "There is a problem in gen_traj_walking() function" + "\033[0m")
        return

    if int(result['count']) >= 1:
        print("Exist walking path.")
    else:
        print("No exist walking path!")

    steps = result['route']['paths'][0]['steps']
    traj = []
    for step in steps:
        pl_ = step['polyline']
        pl = pl_.split(';')
        for p in pl:
            point = p.split(',')
            traj.append([round(float(point[0]), 6), round(float(point[1]), 6)])
    print("Walking trajectory is:")
    print(traj)
    return traj


def gen_traj_bicycling(st, en):
    """
    计算以骑行的方式从 st 到 en 位置的导航轨迹
    :param st: [lng, lat]
    :param en: [lng, lat]
    :return: [[lng_1, lat_1],[lng_2, lat_2], ..., [lng_n, lat_n]]
    """
    ori = str(st[0]) + ',' + str(st[1])
    des = str(en[0]) + ',' + str(en[1])
    params_bicycling['origin'] = ori
    params_bicycling['destination'] = des

    response = requests.get(url=url_bicycling, params=params_bicycling)
    result = response.json()
    if result['status'] == '0':
        print("\033[91m" + "There is a problem in gen_traj_bicycling() function" + "\033[0m")
        return

    if int(result['count']) >= 1:
        print("Exist bicycling path.")
    else:
        print("No exist bicycling path!")

    steps = result['route']['paths'][0]['steps']
    traj = []
    for step in steps:
        pl_ = step['polyline']
        pl = pl_.split(';')
        for p in pl:
            point = p.split(',')
            traj.append([round(float(point[0]), 6), round(float(point[1]), 6)])
    print("Bicycling trajectory is:")
    print(traj)
    return traj


def gen_traj_driving(st, en):
    """
    计算以开车的方式从 st 到 en 位置的导航轨迹
    :param st: [lng, lat]
    :param en: [lng, lat]
    :return: [[lng_1, lat_1],[lng_2, lat_2], ..., [lng_n, lat_n]]
    """
    ori = str(st[0]) + ',' + str(st[1])
    des = str(en[0]) + ',' + str(en[1])
    params_driving['origin'] = ori
    params_driving['destination'] = des

    response = requests.get(url=url_driving, params=params_driving)
    result = response.json()
    if result['status'] == '0':
        print("\033[91m" + "There is a problem in gen_traj_driving() function" + "\033[0m")
        return

    if int(result['count']) >= 1:
        print("Exist driving path.")
    else:
        print("No exist driving path!")

    steps = result['route']['paths'][0]['steps']
    traj = []
    for step in steps:
        pl_ = step['polyline']
        pl = pl_.split(';')
        for p in pl:
            point = p.split(',')
            traj.append([round(float(point[0]), 6), round(float(point[1]), 6)])
    print("Driving trajectory is:")
    print(traj)
    return traj


# -------------------------- Other API ------------------------------
def insert_points(start, end, k):
    """
    在两个坐标之间坤云的插入 k 个点
    :param start: [lng, lat]
    :param end: [lng, lat]
    :param k: 插入个数
    :return: [[lng_1, lat_1], [lng_2, lat_2], ..., [lng_n, lat_n]]
    """

    x_diff = (end[0] - start[0]) / (k + 1)
    y_diff = (end[1] - start[1]) / (k + 1)
    x_values = [start[0] + i * x_diff for i in range(1, k+1)]
    y_values = [start[1] + i * y_diff for i in range(1, k+1)]
    return [[round(x, 6), round(y, 6)] for x, y in zip(x_values, y_values)]


if __name__ == '__main__':
    my_loc = [116.330567, 39.975462]
    get_gaode_loc(my_loc)

