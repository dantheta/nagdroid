#!/usr/bin/env python2.5

import time
import pprint
import xml.etree.ElementTree as et

NAGIOS_STATUS_PATH = '/usr/local/nagios/var/status.dat'

HOSTS = {}

def cvt_time(s):
	return time.asctime(time.gmtime(int(s)))

def parse_nagios_status():
	fp = open(NAGIOS_STATUS_PATH)

	while True:
		buf = fp.readline()
		if buf == '':
			break
		line = buf.strip()
		if line == 'servicestatus {':
			servicedata = {}
			while True:
				line2 = fp.readline().strip()
				if line2 == '}':
					break
				k,v = line2.split('=', 1)
				servicedata[k] = v
			HOSTS.setdefault( servicedata['host_name'], {})[ servicedata['service_description'] ] = servicedata
	fp.close()

def print_xml():
	doc  = et.Element('status')
	for host, hostdata in HOSTS.iteritems():

		if not len([ x for x in hostdata.itervalues() if x['current_state'] != '0' ]):
			continue

		host_el = et.Element('host')
		host_el.attrib['name'] = host
		
		for service, data in hostdata.iteritems():
			if data['current_state'] == '0':
				continue
			srv_el = et.Element('service')
			srv_el.attrib.update({
				'name': data['service_description'],
				'state': data['current_state'],
				'output': data['plugin_output'],
				'last_check': cvt_time(data['last_check']),
				'last_change': cvt_time(data['last_state_change']),
				'last_ok': cvt_time(data['last_time_ok']),
			})
			host_el.append(srv_el)

		host_el.attrib['warning'] = str(len([ x for x in hostdata.itervalues() if x['current_state'] == '1']))
		host_el.attrib['critical'] = str(len([ x for x in hostdata.itervalues() if x['current_state'] == '2']))
		doc.append(host_el)
	print et.tostring(doc)


if __name__ == '__main__':
	print "Content-type: text/xml\n"

	parse_nagios_status()
	print_xml()
