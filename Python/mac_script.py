import paramiko
# from getpass import getpass
import time

def main(ip,port):
    k="int "+port+" \n"
    username = ""
    password = ""

    remote_conn_pre = paramiko.SSHClient()
    remote_conn_pre.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    remote_conn_pre.connect(ip, port=22, username=username, password=password, look_for_keys=False, allow_agent=False)

    remote_conn = remote_conn_pre.invoke_shell()
    # output = remote_conn.recv(65535)
    # print(output)

    # remote_conn.send("show log\n")
    # time.sleep(.5)
    # output = remote_conn.recv(65535)
    # print(output)

    remote_conn.send("\nconfigure terminal\n")
    time.sleep(.5)
    output = remote_conn.recv(65535)
    # print(output)

    remote_conn.send(k)
    time.sleep(.5)
    output = remote_conn.recv(65535)
    # print(output)

    # remote_conn.send("switchport mode access\n")
    # time.sleep(.5)
    # output = remote_conn.recv(65535)
    # print(output)

    #######no port security#############
    remote_conn.send("no switchport port-security\n")
    time.sleep(.5)
    output = remote_conn.recv(65535)
    # print(output)

    remote_conn.send("no switchport port-security mac-address sticky\n")
    time.sleep(.5)
    output = remote_conn.recv(65535)
    # print(output)

    remote_conn.send("no switchport port-security violation restrict\n")
    time.sleep(.5)
    output = remote_conn.recv(65535)
    # print(output)
    #######no port security#############

    #######port security#############
    remote_conn.send("switchport port-security\n")
    time.sleep(.5)
    output = remote_conn.recv(65535)
    # print(output)

    remote_conn.send("switchport port-security mac-address sticky\n")
    time.sleep(.5)
    output = remote_conn.recv(65535)
    # print(output)

    remote_conn.send("switchport port-security violation restrict\n")
    time.sleep(.5)
    output = remote_conn.recv(65535)
    # print(output)
    #######port security#############

    remote_conn.send("exit\n")
    time.sleep(.5)
    output = remote_conn.recv(65535)
    # print(output)

    remote_conn.send("exit\n")
    time.sleep(.5)
    output = remote_conn.recv(65535)
    # print(output)

    remote_conn.send("wr\n")
    time.sleep(.5)
    output = remote_conn.recv(65535)
    # print(output)

    # remote_conn.send(" \n")
    # time.sleep(.5)
    # output = remote_conn.recv(65535)
    # print(output)


# Using the special variable
# __name__
if __name__ == "__main__":
    ip = "10.24.63.11"
    list = ["FastEthernet0/19"]
    for port in list:
        main(ip,port)
