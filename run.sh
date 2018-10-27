uuid="`osqueryi --json 'select uuid from system_info;' | tr -cd '0-9A-Z-'`"
ENDPOINT='localhost:8080'

function query {
  OUTPUT=`osqueryi --json "$1"`
  curl -X POST "$ENDPOINT/device?id=$uuid&type=$2" -H 'Content-Type: application/json' --data "$OUTPUT"
}

query "SELECT * FROM kernel_extensions WHERE name NOT LIKE '%com.apple%';" "kernel_extensions" &
query "SELECT * FROM chrome_extensions;" "chrome_extensions" &
query "SELECT * FROM browser_plugins;" "browser_plugins" &
query "select PID,process_open_sockets.state,processes.name,local_address,local_port,remote_address,remote_port,processes.path from process_open_sockets LEFT JOIN processes USING (pid) WHERE process_open_sockets.state = 'LISTEN' OR process_open_sockets.state = 'ESTABLISHED';" "networked_processes" &
query "SELECT * FROM system_info;" "system_info" &
query "SELECT * FROM wifi_status;" "wifi_status" &
