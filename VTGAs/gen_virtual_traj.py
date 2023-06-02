import data_167
import os
from geopy.distance import geodesic
import numpy as np
import random
from datetime import datetime

from public_api import gen_traj_bicycling, gen_traj_walking, gen_traj_driving
from public_api import get_gaode_loc, insert_points


# Virtual Trajectory Generation Algorithm: 虚拟轨迹生成算法
# 输入：一条真实轨迹    输出：一条虚假轨迹


def init_(traj):
    seq_speed = []
    dis_all = 0
    tim_all = 0
    for i, val in enumerate(traj):
        if i == 0:
            continue
        point1 = (traj[i][1], traj[i][0])
        point2 = (traj[i - 1][1], traj[i - 1][0])
        dis = geodesic(point1, point2).meters
        dis_all += dis
        tim = traj[i][2] - traj[i - 1][2]
        tim_all += tim
        if tim > 0.0001:
            seq_speed.append(round(dis / tim, 2))
        else:
            continue

    ave_speed = round(dis_all / tim_all, 2)
    return ave_speed, seq_speed


def insert_loc(nav):
    res = [nav[0]]
    for i, val in enumerate(nav):
        if i == 0:
            continue
        point1 = (nav[i][1], nav[i][0])
        point2 = (nav[i - 1][1], nav[i - 1][0])
        dis = geodesic(point1, point2).meters
        k = round(dis / 2) - 1  # 每隔2米插入一个点
        if k < 1:
            res.append(nav[i])
            continue
        else:
            t = insert_points(nav[i - 1], nav[i], k)
            res += t
            res.append(nav[i])
    return res


def shape_obf(nav):
    def cal_angle(p1, p2, p3):
        a = np.array(p1) * 100000
        b = np.array(p2) * 100000
        c = np.array(p3) * 100000
        ab = b - a
        bc = c - b
        mod_ab = np.linalg.norm(ab)
        mod_bc = np.linalg.norm(bc)
        cos_theta = np.clip(np.dot(ab, bc) / (mod_ab * mod_bc), -1, 1)
        theta = np.arccos(cos_theta) * 180 / np.pi
        return theta

    def get_6(p1, p2, p3, k):  # old version
        a = np.array(p1)
        b = np.array(p2)
        c = np.array(p3)
        mid = ((a + c) / 2 + b) / 2

        x = np.array([a[0], mid[0], c[0]])
        y = np.array([a[1], mid[1], c[1]])
        p = np.polyfit(x, y, 2)
        x_fit = np.linspace(x[0], x[-1], k)
        y_fit = np.polyval(p, x_fit)

        points = np.stack((x_fit, y_fit), axis=1).tolist()
        arr = [[round(num, 6) for num in row] for row in points]
        return arr

    def bezier_curve(p0, p1, p2, t):
        # t is a value between 0 and 1
        return (1 - t) ** 2 * p0 + 2 * (1 - t) * t * p1 + t ** 2 * p2

    def get_k(p1, p2, p3, k):
        p0 = np.array(p1)
        p1 = np.array(p2)
        p2 = np.array(p3)
        t_values = np.linspace(0, 1, num=k)
        points = np.array([bezier_curve(p0, p1, p2, t) for t in t_values]).tolist()
        arr = [[round(num, 6) for num in row] for row in points]
        return arr

    cnt = []
    length = len(nav)
    for i in range(2, length):
        ang = cal_angle(nav[i - 2], nav[i - 1], nav[i])
        if 110 >= ang >= 70:  # 找到前5个和后5个
            cnt.append(i - 1)

    # print(cnt) # 打印拐角ID
    for i in cnt:
        ll = max(0, i - 5)
        rr = min(i + 5, length - 1)
        res = get_k(nav[ll], nav[i], nav[rr], rr - ll + 1)  # 进行拟合插值
        for j in range(0, rr - ll + 1):
            nav[ll + j] = res[j]

    return nav


def speed_obf(nav, seq):
    new_nav = [nav[0]]

    len_seq = len(seq)
    p = 0
    len_nav = len(nav)
    q = 1
    while q < len_nav:
        tar = seq[p] * 3
        dis = geodesic((nav[q][1], nav[q][0]), (new_nav[-1][1], new_nav[-1][0])).meters
        if abs(dis - tar) < 1.1:
            new_nav.append(nav[q])
            p = (p + 1) % len_seq
            q = q + 1
        else:
            q = q + 1
    return new_nav


def geo_obf(nav):

    for i in range(len(nav)):
        x = random.randint(-20, 20) / 800000  # 1~10
        y = random.randint(-20, 20) / 800000  # 1~10
        nav[i][0] = round(nav[i][0] + x, 6)
        nav[i][1] = round(nav[i][1] + y, 6)
    return nav


def get_false_from_true(real_traj):
    """
    输入一条 real_traj，输出与之对应的 false_traj
    :param real_traj: [[lng_1, lat_1, t_1], ..., [lng_n, lat_n, t_n]]
    :return: [[lng_1, lat_1, t_1], ..., [lng_n, lat_n, t_n]]
    """
    print("真实轨迹：", len(real_traj), real_traj)

    # 1. 速度 和 速度列表
    speed_ave, speed_seq = init_(real_traj)
    print("AVE速度，MED速度，速度序列：", speed_ave, np.median(np.array(speed_seq)), speed_seq)

    # 2. 获得 开始位置 和 结束位置（高德MAP）
    st = [real_traj[0][0], real_traj[0][1]]
    en = [real_traj[-1][0], real_traj[-1][1]]
    print("转换前：", st, en)

    st = get_gaode_loc(st)[0]
    en = get_gaode_loc(en)[0]
    print("转换后", st, en)

    # 3. 获得导航轨迹
    nav_traj = []
    if speed_ave < 3:
        nav_traj = gen_traj_walking(st, en)
    elif 10 >= speed_ave >= 3:
        nav_traj = gen_traj_bicycling(st, en)
    else:
        nav_traj = gen_traj_driving(st, en)

    tmp_ = []
    for i, val in enumerate(nav_traj):
        if i == 0:
            continue
        if geodesic((nav_traj[i - 1][1], nav_traj[i - 1][0]), (val[1], val[0])).meters < 6:
            continue
        tmp_.append(val)

    nav_traj = tmp_
    print("导航轨迹：", nav_traj)

    # 4. 密集插点
    tmp = insert_loc(nav_traj)
    print("密集插点：", tmp)

    # 5. shape混淆
    tmp = shape_obf(tmp)
    print("形状混淆：", tmp)

    # 6. 速度混淆
    # 假设时间戳的间隔是 3s
    tmp = speed_obf(tmp, speed_seq)
    print("速度混淆：", tmp)

    # 7. 噪声
    tmp = geo_obf(tmp)
    print("噪声混淆（无时间戳）：", tmp)

    # 8. 添加时间戳， 3s 一个点；false_traj 为最后生成的虚假轨迹
    false_traj = []
    for i, val in enumerate(tmp):
        false_traj.append([val[0], val[1], i * 3])
    print("虚假轨迹", false_traj)

    return false_traj


# file process
anchor_time = datetime.strptime("2008/01/01 00:00:00", "%Y/%m/%d %H:%M:%S")


def get_a_trajectory(path):
    path_ = path
    pts = []
    pts_no = []
    fp = open(path_, "r")
    for line in fp:
        line = line[0: len(line) - 1]
        line_list = line.split(",")

        lng = round(float(line_list[1]), 6)
        lat = round(float(line_list[0]), 6)
        tim = round(float(line_list[2]), 1)
        pts.append([lat, lng, tim])
        pts_no.append([lat, lng])
    fp.close()
    return pts


def store_path(points, path):
    new_path = path
    with open(new_path, 'w') as fp:
        for p in points:
            fp.writelines(str(p[0]) + "," + str(p[1]) + "," + str(p[2]) + "\n")
    if os.path.getsize(new_path) == 0:
        os.remove(new_path)


if __name__ == '__main__':

    # real trajectory folder
    path = "D:\\Paper\\used_data\\real"
    file_list = os.listdir(path)
    files = []
    for f in file_list:
        next_path = path + "\\" + f
        files.append(next_path)

    for i, f in enumerate(files):
        print("\033[31mFile\033[0m", i)

        a = get_a_trajectory(f)     # 读取真实轨迹, read real trajectory
        b = get_false_from_true(a)  # 输出虚假轨迹, output virtual trajectory
        if len(b) < 20:
            os.remove(f)
            continue

        for val in b:
            val[2] += a[0][2]
        p = "D:\\Paper\\used_data\\false\\" + str(i) + ".txt" # store path
        store_path(b, p)



