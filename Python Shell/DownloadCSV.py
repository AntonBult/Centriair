class DownloadCSVFiles(object):
    def __init__(running):
        running.current = 1

    def GetCSV():
        if running.current == 1:
            subprocess.call()
        else:
            return "Init not completed"
        
