export interface RiskAssignment {
  id: string;
  employee: string;
  company: string;
  riskName: string;
  riskCategory: string;
  exposureLevel: 'low' | 'medium' | 'high';
  assignmentDate: string;
  protectionMeasures: string;
  status: 'active' | 'monitored' | 'resolved';
};