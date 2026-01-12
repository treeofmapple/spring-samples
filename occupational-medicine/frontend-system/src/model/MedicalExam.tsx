type ExamType = '' | 'admission' | 'periodic' | 'return_to_work' | 'dismissal';

export interface MedicalExam {
  id: string;
  employee: string;
  company: string;
  type: ExamType;
  date: string;
  time: string;
  doctor: string;
  status: 'scheduled' | 'in_progress' | 'completed' | 'cancelled';
  result?: 'fit' | 'unfit' | 'fit_with_restrictions';
  observations?: string;
};
