/*     */ package com.anyframe.centercut.core.domain;
/*     */ 
/*     */ import com.anyframe.centercut.core.domain.status.ChunkStatus;
/*     */ import com.anyframe.centercut.core.domain.status.JobInstanceStatus;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ 
/*     */ public class JobInstance implements Comparable<JobInstance>, Serializable {
/*     */   private static final long serialVersionUID = 3346545776777702623L;
/*     */   
/*     */   private final String jobId;
/*     */   
/*     */   private final String execDate;
/*     */   
/*     */   private final int execSeq;
/*     */   
/*     */   private String appId;
/*     */   
/*     */   private int appSeq;
/*     */   
/*  54 */   private JobInstanceStatus instanceStatus = JobInstanceStatus.INIT;
/*     */   
/*     */   private final JobConfig jobConfig;
/*     */   
/*  64 */   private JobInstanceStats jobInstanceStats = new JobInstanceStats();
/*     */   
/*  69 */   private JobInstanceHistory jobInstanceHistory = new JobInstanceHistory();
/*     */   
/*  76 */   private List<Chunk> chunks = new ArrayList<Chunk>();
/*     */   
/*     */   private Chunk headChunk;
/*     */   
/*  90 */   private final Queue<Chunk> executionChunks = new PriorityQueue<Chunk>();
/*     */   
/*     */   private Chunk tailChunk;
/*     */   
/* 102 */   private long startTime = 0L;
/*     */   
/* 107 */   private int reprocessCount = -1;
/*     */   
/* 112 */   private Map<String, String> parameters = new HashMap<String, String>();
/*     */   
/* 117 */   private String exitMessage = "";
/*     */   
/*     */   public JobInstance(String jobId, String execDate, int execSeq, JobConfig jobConfig) {
/* 128 */     this.jobId = jobId;
/* 129 */     this.execDate = execDate;
/* 130 */     this.execSeq = execSeq;
/* 131 */     this.jobConfig = jobConfig;
/*     */   }
/*     */   
/*     */   public JobInstanceStats getJobInstanceStats() {
/* 140 */     return this.jobInstanceStats;
/*     */   }
/*     */   
/*     */   public void setJobInstanceStats(JobInstanceStats jobInstanceStats) {
/* 149 */     this.jobInstanceStats = jobInstanceStats;
/*     */   }
/*     */   
/*     */   public String getJobId() {
/* 158 */     return this.jobId;
/*     */   }
/*     */   
/*     */   public String getExecDate() {
/* 167 */     return this.execDate;
/*     */   }
/*     */   
/*     */   public int getExecSeq() {
/* 176 */     return this.execSeq;
/*     */   }
/*     */   
/*     */   public JobInstanceStatus getInstanceStatus() {
/* 185 */     return this.instanceStatus;
/*     */   }
/*     */   
/*     */   public JobConfig getJobConfig() {
/* 194 */     return this.jobConfig;
/*     */   }
/*     */   
/*     */   public List<Chunk> getChunks() {
/* 203 */     return this.chunks;
/*     */   }
/*     */   
/*     */   public void setChunks(List<Chunk> chunks) {
/* 212 */     this.chunks = chunks;
/*     */   }
/*     */   
/*     */   public void setInstanceStatus(JobInstanceStatus instanceStatus) {
/* 221 */     this.instanceStatus = instanceStatus;
/*     */   }
/*     */   
/*     */   public Chunk popChunk() {
/* 230 */     Chunk chunk = this.executionChunks.poll();
/* 232 */     if (chunk != null)
/* 233 */       chunk.setParameters(this.parameters); 
/* 236 */     return chunk;
/*     */   }
/*     */   
/*     */   public void putChunk(Chunk chunk) {
/* 245 */     this.executionChunks.add(chunk);
/*     */   }
/*     */   
/*     */   public boolean isNextChunkEmpty() {
/* 254 */     return this.executionChunks.isEmpty();
/*     */   }
/*     */   
/*     */   public void initExecution() {
/* 261 */     this.startTime = System.currentTimeMillis();
/* 263 */     this.executionChunks.clear();
/* 265 */     this.reprocessCount++;
/* 267 */     for (Chunk chunk : this.chunks) {
/* 268 */       chunk.setReprocessCount(this.reprocessCount);
/* 270 */       if (chunk.isHeadChunk()) {
/* 271 */         this.headChunk = chunk;
/*     */         continue;
/*     */       } 
/* 275 */       if (chunk.isTailChunk()) {
/* 276 */         this.tailChunk = chunk;
/*     */         continue;
/*     */       } 
/* 280 */       if (chunk.getChunkStatus() == ChunkStatus.READY)
/* 281 */         this.executionChunks.add(chunk); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isStopping() {
/* 292 */     if (this.instanceStatus == JobInstanceStatus.STOPPING)
/* 293 */       return true; 
/* 295 */     return false;
/*     */   }
/*     */   
/*     */   public int getReprocessCount() {
/* 305 */     return this.reprocessCount;
/*     */   }
/*     */   
/*     */   public void setReprocessCount(int reprocessCount) {
/* 314 */     this.reprocessCount = reprocessCount;
/*     */   }
/*     */   
/*     */   public JobInstanceHistory getJobInstanceHistory() {
/* 323 */     return this.jobInstanceHistory;
/*     */   }
/*     */   
/*     */   public void setJobInstanceHistory(JobInstanceHistory jobInstanceHistory) {
/* 332 */     this.jobInstanceHistory = jobInstanceHistory;
/*     */   }
/*     */   
/*     */   public Map<String, String> getParameters() {
/* 341 */     return this.parameters;
/*     */   }
/*     */   
/*     */   public void setParameters(Map<String, String> parameters) {
/* 350 */     this.parameters = parameters;
/*     */   }
/*     */   
/*     */   public String getAppId() {
/* 359 */     return this.appId;
/*     */   }
/*     */   
/*     */   public void setAppId(String appId) {
/* 368 */     this.appId = appId;
/*     */   }
/*     */   
/*     */   public int getAppSeq() {
/* 377 */     return this.appSeq;
/*     */   }
/*     */   
/*     */   public void setAppSeq(int appSeq) {
/* 386 */     this.appSeq = appSeq;
/*     */   }
/*     */   
/*     */   public long getStartTime() {
/* 395 */     return this.startTime;
/*     */   }
/*     */   
/*     */   public Queue<Chunk> getExecutionChunks() {
/* 404 */     return this.executionChunks;
/*     */   }
/*     */   
/*     */   public String getExitMessage() {
/* 408 */     return this.exitMessage;
/*     */   }
/*     */   
/*     */   public void setExitMessage(String exitMessage) {
/* 412 */     this.exitMessage = exitMessage;
/*     */   }
/*     */   
/*     */   public int compareTo(JobInstance other) {
/* 417 */     if (this.startTime < other.startTime)
/* 418 */       return -1; 
/* 420 */     return 1;
/*     */   }
/*     */   
/*     */   public Chunk getHeadChunk() {
/* 425 */     return this.headChunk;
/*     */   }
/*     */   
/*     */   public Chunk getTailChunk() {
/* 429 */     return this.tailChunk;
/*     */   }
/*     */   
/*     */   public String getKey() {
/* 433 */     return this.jobId + this.execDate + this.execSeq;
/*     */   }
/*     */   
/*     */   public String toKeyString() {
/* 442 */     return "[jobId=" + this.jobId + ", execDate=" + this.execDate + ", execSeq=" + this.execSeq + "]";
/*     */   }
/*     */   
/*     */   public String toString() {
/* 447 */     return "JobInstance [jobId=" + this.jobId + ", execDate=" + this.execDate + ", execSeq=" + this.execSeq + ", appId=" + this.appId + ", appSeq=" + this.appSeq + ", instanceStatus=" + this.instanceStatus + ", jobConfig=" + this.jobConfig + ", jobInstanceStats=" + this.jobInstanceStats + ", jobInstanceHistory=" + this.jobInstanceHistory + ", chunks=" + this.chunks + ", headChunk=" + this.headChunk + ", executionChunks=" + this.executionChunks + ", tailChunk=" + this.tailChunk + ", startTime=" + this.startTime + ", reprocessCount=" + this.reprocessCount + ", parameters=" + this.parameters + ", exitMessage=" + this.exitMessage + "]";
/*     */   }
/*     */ }


/* Location:              C:\mySingle\Temp\anyframe-centercut-interfaces-5.0.1.jar!\com\anyframe\centercut\core\domain\JobInstance.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */